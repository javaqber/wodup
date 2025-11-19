import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservaService } from '../../services/reserva.service';
import { ClaseService } from '../../services/clase.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reservas.component.html',
  styleUrls: ['./reservas.component.css']
})
export class ReservasComponent implements OnInit {
  franjas: { inicio: string; fin: string }[] = [];
  clases: any[] = [];
  claseDeHoy: any = null;
  reservasUsuario: any[] = [];
  mensaje = '';
  error = '';
  
  private debug = true;

  constructor(
    private claseService: ClaseService,
    private reservaService: ReservaService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.generarFranjasFijas();
    this.cargarClasesDeHoy();
    this.cargarReservasUsuario();
  }

  generarFranjasFijas(): void {
    const horas = [
      { inicio: 7, fin: 14 },
      { inicio: 16, fin: 21 }
    ];

    horas.forEach(bloque => {
      for (let h = bloque.inicio; h < bloque.fin; h++) {
        const inicio = `${h.toString().padStart(2, '0')}:00`;
        const fin = `${(h + 1).toString().padStart(2, '0')}:00`;
        this.franjas.push({ inicio, fin });
      }
    });
  }

  cargarClasesDeHoy(): void {
    this.claseService.obtenerClasesDeHoy().subscribe({
      next: (data) => {
        this.clases = data || [];
        this.claseDeHoy = (this.clases.length > 0) ? this.clases[0] : null;
      },
      error: (err) => {
        console.error('Error cargarClasesDeHoy', err);
        this.error = 'Error al cargar las clases de hoy.';
      }
    });
  }

  cargarReservasUsuario(): void {
    this.reservaService.obtenerMisReservas().subscribe({
      next: (data) => {
        this.reservasUsuario = data || [];
        if (this.debug) {
          console.log('--- RESERVAS CARGADAS DEL BACKEND ---', this.reservasUsuario);
        }
      },
      error: (err) => {
        console.warn('No se pudieron cargar las reservas del usuario.');
        this.reservasUsuario = [];
      }
    });
  }

  // Convierte cualquier formato de hora del backend a "HH:MM"
  //Maneja: "09:00:00", "09:00", [9, 0]
  private normalizarHora(hora: any): string {
    if (!hora) return '';

    // Caso 1: Array de tiempo [9, 0] o [19, 30] (Típico de Spring Boot JSON)
    if (Array.isArray(hora) && hora.length >= 2) {
        const h = hora[0].toString().padStart(2, '0');
        const m = hora[1].toString().padStart(2, '0');
        return `${h}:${m}`;
    }

    // Caso 2: String "09:00:00" o "19:00"
    if (typeof hora === 'string') {
        return hora.substring(0, 5);
    }

    return '';
  }

  getReservaParaFranja(claseId: number, horaInicioFranja: string): any | null {
    if (!this.reservasUsuario || this.reservasUsuario.length === 0) {
      return null;
    }

    // Buscamos en la lista descargada del servidor
    const reservaEncontrada = this.reservasUsuario.find(r => {
      
      // 1. Normalizamos la hora que viene del backend usando el helper
      const horaReservaStr = this.normalizarHora(r.horaInicio);

      // 2. Comprobamos IDs (soporta objeto anidado o ID plano)
      const idEnReserva = r.clase ? r.clase.id : (r.claseId || 0);
      
      const matchClase = (idEnReserva === claseId);
      const matchEstado = (r.estado === 'CONFIRMADA');
      // Comparamos strings normalizados ("19:00" === "19:00")
      const matchHora = (horaReservaStr === horaInicioFranja);

      return matchClase && matchEstado && matchHora;
    });
    
    return reservaEncontrada || null;
  }

  // ... (Resto de métodos auxiliares sin cambios)
  obtenerClasePorFranja(franja: { inicio: string; fin: string }) {
    const hoy = new Date().toISOString().split('T')[0];
    return this.clases.find(c =>
      c.fecha === hoy &&
      c.horaInicio?.substring(0, 5) === franja.inicio &&
      c.horaFin?.substring(0, 5) === franja.fin
    );
  }

  capacidadDisponible(clase: any): boolean {
    return (clase.reservas && clase.reservas.length < clase.capacidad) || (!clase.reservas && clase.capacidad > 0);
  }

  franjaPasada(horaInicio: string): boolean {
    const ahora = new Date();
    const fechaClase = new Date();
    const [h, m] = horaInicio.split(':').map(Number);
    fechaClase.setHours(h, m, 0, 0);
    return fechaClase.getTime() < ahora.getTime();
  }

  reservarClase(clase: any, franja: { inicio: string; fin: string }): void {
    const body = {
      claseId: clase.id,
      horaInicio: franja.inicio,
      horaFin: franja.fin
    };

    this.reservaService.crearReserva(body).subscribe({
      next: (reservaGuardada: any) => {
        this.mensaje = 'Clase reservada correctamente ✅';
        
        // Inyectamos datos manuales para actualización inmediata
        const reservaParaUI = {
            ...reservaGuardada,
            clase: clase,
            claseId: clase.id,
            // Aseguramos que localmente tenga el formato string correcto
            horaInicio: franja.inicio, 
            estado: 'CONFIRMADA'
        };

        this.reservasUsuario = [...this.reservasUsuario, reservaParaUI];
        setTimeout(() => this.mensaje = '', 3000); 
      },
      error: (err) => {
        console.error('Error reservarClase', err);
        if (err.status === 409 || err.status === 401) {
             this.error = 'No se pudo reservar. Verifica si ya tienes una clase activa hoy.';
        } else {
             this.error = err.error?.message || 'Error al reservar la clase.';
        }
        setTimeout(() => this.error = '', 3000);
      }
    });
  }

  cancelarReserva(reservaId: number): void {
    if (!reservaId) {
      this.error = 'Error: ID de reserva no encontrado.';
      return;
    }
    
    this.reservaService.cancelarReserva(reservaId).subscribe({
      next: () => {
        this.mensaje = 'Reserva cancelada correctamente ❌';
        // Recargamos del backend para asegurar estado consistente
        this.cargarReservasUsuario();
        setTimeout(() => this.mensaje = '', 3000);
      },
      error: (err) => {
        console.error('Error cancelarReserva', err);
        this.error = err.error?.message || 'Error al cancelar la reserva.';
        setTimeout(() => this.error = '', 3000);
      }
    });
  }
}