import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClaseService {
  private baseUrl = 'http://localhost:8080/api/clases';

  constructor(private http: HttpClient) {}

  obtenerClases(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  // Obtener las clases del día actual (para la página /reservas)
  obtenerClasesDeHoy(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/hoy`);
  }

  crearClase(claseData: any): Observable<any> {
    return this.http.post(this.baseUrl, claseData);
  }

  actualizarClase(id: number, claseData: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, claseData);
  }

  eliminarClase(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
