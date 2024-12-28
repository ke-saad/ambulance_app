import { TestBed } from '@angular/core/testing';

import { AmbulanceServiceService } from './ambulance-service.service';

describe('AmbulanceServiceService', () => {
  let service: AmbulanceServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AmbulanceServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
