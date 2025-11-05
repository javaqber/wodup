import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:8080/api'; // URL de tu backend

  constructor(private http: HttpClient) { }

  // ===========================
  // AUTH
  // ===========================

  // Login
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/login`, { email, password });
  }

  // Registro
  register(usuario: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/register`, usuario);
  }

  // Guardar datos de usuario y token en localStorage
  guardarSesion(usuarioData: any) {
    localStorage.setItem('token', usuarioData.token);
    localStorage.setItem('usuario', JSON.stringify({
      id: usuarioData.id,
      nombre: usuarioData.nombre,
      apellido: usuarioData.apellido,
      email: usuarioData.email,
      rol: usuarioData.rol
    }));
  }

  // Obtener usuario actual desde localStorage
  obtenerUsuarioActual() {
    const usuario = localStorage.getItem('usuario');
    return usuario ? JSON.parse(usuario) : null;
  }

  // Logout
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
  }

  // Obtener usuario actual usando JWT desde backend
  getCurrentUser(): Observable<any> {
    return this.http.get(`${this.baseUrl}/users/me`);
  }

  // ===========================
  // RESERVAS
  // ===========================

  // Obtener reservas del usuario logueado
  getMisReservas(): Observable<any> {
    return this.http.get(`${this.baseUrl}/reservas/mis`);
  }

  // Crear reserva de clase
  reservarClase(claseId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/reservas/${claseId}`, null);
  }

  // Cancelar reserva
  cancelarReserva(reservaId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/reservas/${reservaId}`);
  }

  // ===========================
  // HORARIO
  // ===========================

  getHorario(): Observable<any> {
    return this.http.get(`${this.baseUrl}/horario`);
  }
}
