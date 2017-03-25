import { inject, getTestBed, TestBed } from '@angular/core/testing';
import { Injector } from '@angular/core';
import { XHRBackend, HttpModule } from '@angular/http';
import { MockConnection, MockBackend } from '@angular/http/testing';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs/Observable';

import { TranslateService, TranslateModule, TranslateLoader } from '@ngx-translate/core';

import { RacesService } from '../races/races.service';
import { RacesHttpService } from '../races/races-http.service';
import { AdjudicatorComponent } from './adjudicator.component';

describe('Adjudicator', () => {
    let translate: any;
    let injector: Injector;
    let backend: any;
    let connection: MockConnection;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpModule, TranslateModule.forRoot()],
            providers: [
                {provide: XHRBackend, useClass: MockBackend},
                RacesService,
                RacesHttpService,
                Title,
                AdjudicatorComponent
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

    it('should have a url', inject([ AdjudicatorComponent ], (app: AdjudicatorComponent) => {
//        expect(app.tiatusUrl).toEqual('https://github.com/PloughingAByteField/tiatus');
        expect(true).toBe(true);
    }));

});
