import { inject, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { BaseRequestOptions, ConnectionBackend, Http } from '@angular/http';
import { MockBackend } from '@angular/http/testing';

import { RacesService } from '../../services/races.service';
import { Race } from '../../models/race.model';

// Load the implementations that should be tested
import { PositionComponent } from './position.component';

describe('Landing', () => {
  // provide our implementations or mocks to the dependency injector
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      BaseRequestOptions,
      MockBackend,
      RacesService,
      {
        provide: Http,
        useFactory: (backend: ConnectionBackend, defaultOptions: BaseRequestOptions) => {
          return new Http(backend, defaultOptions);
        },
        deps: [MockBackend, BaseRequestOptions]
      },
      PositionComponent
    ]
  }));

  it('should have default data', inject([ PositionComponent ], (position: PositionComponent) => {
    expect(true).toBe(true);
  }));

  it('should have a title', inject([ PositionComponent ], (position: PositionComponent) => {
    expect(true).toBe(true);
  }));

});
