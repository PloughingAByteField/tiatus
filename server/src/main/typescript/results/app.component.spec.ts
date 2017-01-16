import { inject, getTestBed, TestBed } from '@angular/core/testing';
import { Injector } from "@angular/core";
import { Component } from '@angular/core';
import { BaseRequestOptions, ConnectionBackend, Http, XHRBackend, HttpModule } from '@angular/http';
import { MockConnection, MockBackend } from '@angular/http/testing';
import { Title }     from '@angular/platform-browser';
import { RacesService } from '../services/races.service';
import {
    TranslateService,
    MissingTranslationHandler,
    MissingTranslationHandlerParams,
    TranslateLoader,
    TranslateStaticLoader,
    LangChangeEvent,
    TranslationChangeEvent, TranslateModule
} from 'ng2-translate';

import { AppComponent } from './app.component';

import {Observable} from "rxjs/Observable";

describe('App', () => {
    let translate: TranslateService;
    let injector: Injector;
    let backend: MockBackend;
    let connection: MockConnection; // this will be set when a new connection is emitted from the backend.

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpModule, TranslateModule.forRoot()],
            providers: [
                {provide: XHRBackend, useClass: MockBackend},
                RacesService,
                Title,
                AppComponent
            ]
        });

        injector = getTestBed();
        backend = injector.get(XHRBackend);
        translate = injector.get(TranslateService);
        // sets the connection when someone tries to access the backend with an xhr request
        backend.connections.subscribe((c: MockConnection) => connection = c);
    });

    afterEach(() => {
        injector = undefined;
        backend = undefined;
        translate = undefined;
        connection = undefined;
    });

  it('should have a url', inject([ AppComponent ], (app: AppComponent) => {
    expect(app.tiatusUrl).toEqual('https://github.com/PloughingAByteField/tiatus');
  }));

});
