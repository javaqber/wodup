import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClaseService } from '../../services/clase.service';

@Component({
  selector: 'app-clases',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clases.component.html',
  styleUrls: ['./clases.component.css']
})
export class ClasesComponent implements OnInit {
  clases: any[] = [];
  nuevaClase: any = {};
  claseEditando: any = null;
  mostrarFormulario = false;
  error: string | null = null;

  constructor(private clasesService: ClaseService) {}

  ngOnInit(): void {
    this.cargarClases();
  }

  cargarClases(): void {
    this.clasesService.obtenerClases().subscribe({
      next: (data) => {
        this.clases = data;
      },
      error: () => {
        this.error = 'Error al cargar las clases.';
      }
    });
  }

  /** Crea o actualiza según el contexto **/
  crearOActualizarClase(): void {
    if (this.claseEditando) {
      // Editar
      this.clasesService.actualizarClase(this.claseEditando.id, this.nuevaClase).subscribe({
        next: () => {
          this.cargarClases();
          this.cancelarEdicion();
        },
        error: () => this.error = 'Error al actualizar la clase.'
      });
    } else {
      // Crear
      this.clasesService.crearClase(this.nuevaClase).subscribe({
        next: () => {
          this.cargarClases();
          this.cancelarEdicion();
        },
        error: () => this.error = 'Error al crear la clase.'
      });
    }
  }

  
  editarClase(clase: any): void {
    console.log('Editando clase:', clase);
    this.claseEditando = clase;
    this.nuevaClase = { ...clase };
    this.mostrarFormulario = true;
  }

  
  nuevaClaseVacia(): void {
    this.nuevaClase = {};
    this.claseEditando = null;
    this.mostrarFormulario = true;
  }

  eliminarClase(id: number): void {
    if (!confirm('¿Seguro que quieres eliminar esta clase?')) return;

    this.clasesService.eliminarClase(id).subscribe({
      next: () => this.cargarClases(),
      error: () => this.error = 'Error al eliminar la clase.'
    });
  }

  cancelarEdicion(): void {
    this.mostrarFormulario = false;
    this.claseEditando = null;
    this.nuevaClase = {};
  }
}
