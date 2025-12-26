import { TestBed } from '@angular/core/testing';

import { Clase } from './clase';

describe('Clase', () => {
  let service: Clase;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Clase);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
