import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service.ts';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit() {
    if (!this.email || !this.password) {
      this.errorMessage = 'Por favor, complete todos los campos';
      return;
    }

    this.authService.login(this.email, this.password).subscribe({
      next: (response: any) => {
        // Guardar sesión
        this.authService.logout(); // Limpiar sesión previa
        this.authService.login(this.email, this.password).subscribe();
        localStorage.setItem('token', response.token);
        localStorage.setItem('usuario', JSON.stringify({
          id: response.id,
          nombre: response.nombre,
          apellido: response.apellido,
          email: response.email,
          rol: response.rol
        }));

        // Redirigir al Home
        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Error al iniciar sesión';
      }
    });
  }
}
