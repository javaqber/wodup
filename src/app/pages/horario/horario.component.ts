import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-horario',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './horario.component.html',
  styleUrls: ['./horario.component.css']
})
export class HorarioComponent implements OnInit {

  clasesManana = [
    { hora: '07:00 - 08:00', tipo: 'CrossFit', coach: 'Laura' },
    { hora: '08:00 - 09:00', tipo: 'WOD', coach: 'Carlos' },
    { hora: '09:00 - 10:00', tipo: 'Open Box', coach: 'Libre' },
    { hora: '10:00 - 11:00', tipo: 'Halterofilia', coach: 'Ana' },
    { hora: '11:00 - 12:00', tipo: 'CrossFit', coach: 'David' },
    { hora: '12:00 - 13:00', tipo: 'Mobility', coach: 'Sara' },
    { hora: '13:00 - 14:00', tipo: 'WOD', coach: 'Mario' }
  ];

  clasesTarde = [
    { hora: '16:00 - 17:00', tipo: 'Open Box', coach: 'Libre' },
    { hora: '17:00 - 18:00', tipo: 'CrossFit', coach: 'Laura' },
    { hora: '18:00 - 19:00', tipo: 'Halterofilia', coach: 'Carlos' },
    { hora: '19:00 - 20:00', tipo: 'WOD', coach: 'David' },
    { hora: '20:00 - 21:00', tipo: 'Mobility', coach: 'Sara' }
  ];

  ngOnInit() {}
}
