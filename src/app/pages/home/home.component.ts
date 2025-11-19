import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservaService } from '../../services/reserva.service';
import { AuthService } from '../../services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  usuario: any = null;
  misReservas: any[] = [];
  loading = true;

  constructor(
    private authService: AuthService,
    private reservaService: ReservaService
  ) {}

  ngOnInit(): void {
    this.usuario = this.authService.getUsuarioActual();
    this.cargarMisReservas();
  }

  // verificar roles y mostrar el botón de Clases
  esAdminOCoach(): boolean {
    if (!this.usuario || !this.usuario.rol) return false;
    const rol = this.usuario.rol; 
    // Ajusta los strings según como se guarden los roles en la DB
    return rol === 'ADMIN' || rol === 'COACH' || rol === 'ROLE_ADMIN' || rol === 'ROLE_COACH';
  }

  cargarMisReservas(): void {
    this.reservaService.obtenerMisReservas().subscribe({
      next: (data) => {
        // Filtramos solo las reservas activas
        this.misReservas = data.filter(r => r.estado === 'CONFIRMADA');
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al cargar reservas en Home', err);
        this.loading = false;
      }
    });
  }
}