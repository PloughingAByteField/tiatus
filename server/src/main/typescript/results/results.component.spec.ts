import { inject, getTestBed, TestBed } from '@angular/core/testing';
import { Injector } from '@angular/core';
import { XHRBackend, HttpModule } from '@angular/http';
import { MockConnection, MockBackend } from '@angular/http/testing';
import { Title }     from '@angular/platform-browser';
import { Observable } from 'rxjs/Observable';
import { TranslateService, TranslateModule } from 'ng2-translate';

import { RacesService } from '../services/races.service';
import { RacesHttpService } from '../http-services/races.service';
import { ResultsComponent } from './results.component';

describe('Results', () => {
    let translate: TranslateService;
    let injector: Injector;
    let backend: MockBackend;
    let connection: MockConnection;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [ HttpModule, TranslateModule.forRoot() ],
            providers: [
                {provide: XHRBackend, useClass: MockBackend},
                RacesService,
                RacesHttpService,
                Title,
                ResultsComponent
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

    it('should have a url', inject([ ResultsComponent ], (app: ResultsComponent) => {
        expect(app.tiatusUrl).toEqual('https://github.com/PloughingAByteField/tiatus');
    }));

});
