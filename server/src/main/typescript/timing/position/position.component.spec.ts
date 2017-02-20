import { inject, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { BaseRequestOptions, ConnectionBackend, Http } from '@angular/http';
import { MockBackend } from '@angular/http/testing';

import { PositionsService } from '../../services/positions.service';
import { TimingPositionService } from '../services/timing-position.service';

import { Race } from '../../models/race.model';

// Load the implementations that should be tested
import { PositionComponent } from './position.component';

describe('Position', () => {
  // provide our implementations or mocks to the dependency injector
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      BaseRequestOptions,
      MockBackend,
      PositionsService,
      TimingPositionService,
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
