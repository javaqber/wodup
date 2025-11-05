import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservaService } from '../../services/reserva.service';
import { ClaseService } from '../../services/clase.service';
import { AuthService } from '../../services/auth.service.ts';

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
  reservasUsuario: any[] = [];
  mensaje = '';
  error = '';

  constructor(
    private claseService: ClaseService,
    private reservaService: ReservaService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.generarFranjasFijas();
    this.cargarClases();
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

  cargarClases(): void {
    this.claseService.obtenerClases().subscribe({
      next: (data) => this.clases = data,
      error: () => this.error = 'Error al cargar las clases.'
    });
  }

  cargarReservasUsuario(): void {
    this.reservaService.obtenerMisReservas().subscribe({
      next: (data) => this.reservasUsuario = data,
      error: () => this.error = 'Error al cargar tus reservas.'
    });
  }

  // Verifica si hay una clase activa en la franja
  obtenerClasePorFranja(franja: { inicio: string; fin: string }) {
    const hoy = new Date().toISOString().split('T')[0];
    return this.clases.find(c =>
      c.fecha === hoy &&
      c.horaInicio.substring(0,5) === franja.inicio &&
      c.horaFin.substring(0,5) === franja.fin
    );
  }

  estaReservado(claseId: number): boolean {
    return this.reservasUsuario.some(r => r.clase.id === claseId);
  }

  capacidadDisponible(clase: any): boolean {
    return clase.reservas && clase.reservas.length < clase.capacidad;
  }

  franjaPasada(horaInicio: string): boolean {
    const ahora = new Date();
    const fechaClase = new Date();
    const [h, m] = horaInicio.split(':').map(Number);
    fechaClase.setHours(h, m, 0, 0);
    return fechaClase.getTime() < ahora.getTime();
  }

  reservarClase(claseId: number): void {
    this.reservaService.crearReserva(claseId).subscribe({
      next: () => {
        this.mensaje = 'Clase reservada correctamente ✅';
        this.cargarReservasUsuario();
      },
      error: () => this.error = 'Error al reservar la clase.'
    });
  }

  cancelarReserva(claseId: number): void {
    this.reservaService.cancelarReserva(claseId).subscribe({
      next: () => {
        this.mensaje = 'Reserva cancelada correctamente ❌';
        this.cargarReservasUsuario();
      },
      error: () => this.error = 'Error al cancelar la reserva.'
    });
  }
}
