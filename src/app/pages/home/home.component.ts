import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  usuario: any = null;
  reservas: any[] = [];
  errorMessage = '';

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.cargarUsuario();
    this.cargarReservas();
  }

  cargarUsuario() {
    this.usuario = this.userService.obtenerUsuarioActual();
    if (!this.usuario) {
      this.router.navigate(['/login']);
    }
  }

  cargarReservas() {
    this.userService.getMisReservas().subscribe({
      next: (data: any[]) => {
        this.reservas = data;
      },
      error: (err) => {
        console.error(err);
        this.reservas = [];
      }
    });
  }

  // =========================
  // MÉTODOS DE RESERVA
  // =========================
  reservarClase(claseId: number) {
    this.userService.reservarClase(claseId).subscribe({
      next: () => this.cargarReservas(),
      error: (err) => {
        console.error(err);
        this.errorMessage = 'No se pudo reservar la clase.';
      }
    });
  }

  cancelarReserva(reservaId: number) {
    this.userService.cancelarReserva(reservaId).subscribe({
      next: () => this.cargarReservas(),
      error: (err) => {
        console.error(err);
        this.errorMessage = 'No se pudo cancelar la reserva.';
      }
    });
  }

  irReservas() {
  this.router.navigate(['/reservas']);
  }

  irHorario() {
  this.router.navigate(['/horario']);
  }

  crearClase(){
    this.router.navigate(['/crear-clase']);
  }
}
