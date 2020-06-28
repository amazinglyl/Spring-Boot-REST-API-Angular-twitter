import { TestBed } from '@angular/core/testing';

import { TokenstoreserviceService } from './tokenstoreservice.service';

describe('TokenstoreserviceService', () => {
  let service: TokenstoreserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenstoreserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
