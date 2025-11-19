import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservaService {
  private apiUrl = 'http://localhost:8080/api/reservas';

  constructor(private http: HttpClient) {}

  // Obtener todas las reservas del usuario autenticado
  obtenerMisReservas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mis`);
  }

  // Crear una nueva reserva
  crearReserva(reservaData: { claseId: number; horaInicio: string; horaFin: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}`, reservaData);
  }

  // Cancelar una reserva
  cancelarReserva(reservaId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${reservaId}`);
  }
}
