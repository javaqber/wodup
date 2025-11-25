import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {
    if (authService) {
      console.log('✅ AuthInterceptor: Servicio AuthService inyectado correctamente.');
    } else {
      console.error('🛑 AuthInterceptor: ¡AuthService NO se ha inyectado! (undefined)');
    }
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log(`🚀 AuthInterceptor: Ejecutándose para la URL: ${req.url}`);
    
    const token = this.authService.getToken();

    // 💡 ¡¡¡NECESITO VER ESTO EN TU CONSOLA!!!
    console.log('--- VALOR DEL TOKEN (DEBUG) ---');
    console.log(token);
    console.log('---------------------------------');

    if (token) {
      console.log('🔑 AuthInterceptor: Token encontrado. Adjuntando header Bearer...');
      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(authReq);
    }

    console.warn(`🚦 AuthInterceptor: No se encontró token para ${req.url}.`);
    return next.handle(req);
  }
}