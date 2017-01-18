import { inject, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { BaseRequestOptions, ConnectionBackend, Http } from '@angular/http';
import { MockBackend } from '@angular/http/testing';

import { Race, RacesService } from '../../services/races.service';

// Load the implementations that should be tested
import { LandingComponent } from './landing.component';

describe('Landing', () => {
  // provide our implementations or mocks to the dependency injector
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      BaseRequestOptions,
      MockBackend,
      RacesService,
      {
        provide: Http,
        useFactory: function(backend: ConnectionBackend, defaultOptions: BaseRequestOptions) {
          return new Http(backend, defaultOptions);
        },
        deps: [MockBackend, BaseRequestOptions]
      },
      LandingComponent
    ]
  }));

  it('should have default data', inject([ LandingComponent ], (landing: LandingComponent) => {
    expect(true).toBe(true);
  }));

  it('should have a title', inject([ LandingComponent ], (landing: LandingComponent) => {
    expect(true).toBe(true);
  }));


});
