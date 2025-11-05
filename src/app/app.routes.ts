import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './pages/home/home.component';
import { ReservasComponent } from './pages/reservas/reservas.component';
import { HorarioComponent } from './pages/horario/horario.component';
import {ClasesComponent} from './components/clases/clases.component'
import { AuthGuard } from './guards/auth.guard';


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'reservas', component: ReservasComponent, canActivate: [AuthGuard] },
  { path: 'horario', component: HorarioComponent, canActivate: [AuthGuard] },
  { path: 'clases', component: ClasesComponent }

];
