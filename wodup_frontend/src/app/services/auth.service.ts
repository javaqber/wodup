import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  //private baseUrl = 'http://localhost:8080/api/auth'; // Conecta con Backend Local
  private baseUrl = `${environment.apiUrl}/auth`;
  private authStatus = new BehaviorSubject<boolean>(this.hasToken()); // Permite detectar cambios en la sesión

  constructor(private http: HttpClient) {}

  // ==================================================
  // LOGIN: autentica y guarda token + datos del usuario
  // ==================================================
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, { email, password }).pipe(
      tap((response: any) => {
        // Guardamos el token y los datos del usuario
        localStorage.setItem('token', response.token);
        localStorage.setItem('usuario', JSON.stringify({
          id: response.id,
          email: response.email,
          nombre: response.nombre,
          apellido: response.apellido,
          rol: response.rol
        }));

        // Actualizamos el estado de autenticación
        this.authStatus.next(true);
      })
    );
  }

  // ==================================================
  // REGISTRO:
  // ==================================================
  register(nombre: string, apellido: string, email: string, password: string): Observable<any> {
  const body = { nombre, apellido, email, password };
  return this.http.post(`${this.baseUrl}/register`, body);
}

  // ==================================================
  // LOGOUT: limpia el almacenamiento local y emite cambio
  // ==================================================
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    this.authStatus.next(false);
  }

  // ==================================================
  // ESTADO DE SESIÓN
  // ==================================================
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  // Versión reactiva: útil si más adelante quieres mostrar “Cerrar sesión” dinámicamente en navbar
  getAuthStatus(): Observable<boolean> {
    return this.authStatus.asObservable();
  }

  // ==================================================
  // DATOS DEL USUARIO Y TOKEN
  // ==================================================
  getUsuarioActual() {
    const user = localStorage.getItem('usuario');
    return user ? JSON.parse(user) : null;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // ==================================================
  // HELPERS
  // ==================================================
  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }
}
