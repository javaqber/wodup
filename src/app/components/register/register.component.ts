import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service.ts';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  nombre = '';
  apellido = '';
  email = '';
  password = '';
  errorMessage = '';
  successMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (!this.nombre || !this.apellido || !this.email || !this.password) {
      this.errorMessage = 'Por favor, complete todos los campos.';
      this.successMessage = '';
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    console.log('🟢 Enviando datos de registro:', this.nombre, this.apellido, this.email);

    this.authService.register(this.nombre, this.apellido, this.email, this.password).subscribe({
      next: (response) => {
        console.log('✅ Usuario registrado correctamente:', response);
        this.successMessage = 'Usuario registrado con éxito. Redirigiendo al inicio de sesión...';
        setTimeout(() => this.router.navigate(['/login']), 3000);
      },
      error: (err) => {
        console.error('❌ Error al registrar usuario:', err);
        if (err.status === 409) {
          this.errorMessage = 'El email ya está en uso.';
        } else {
          this.errorMessage = 'No se pudo registrar el usuario. Intente nuevamente.';
        }
      }
    });
  }
}
