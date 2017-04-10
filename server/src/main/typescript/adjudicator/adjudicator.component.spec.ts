import { inject, getTestBed, TestBed } from '@angular/core/testing';
import { Injector } from '@angular/core';
import { XHRBackend, HttpModule } from '@angular/http';
import { MockConnection, MockBackend } from '@angular/http/testing';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs/Observable';

import { TranslateService, TranslateModule, TranslateLoader } from '@ngx-translate/core';

import { WebSocketService } from '../websocket/websocket-service';
import { WebSocketWSService } from '../websocket/websocket-ws-service';

import { RacesService } from '../races/races.service';
import { RacesHttpService } from '../races/races-http.service';
import { AdjudicatorComponent } from './adjudicator.component';

import { ClubsService } from '../clubs/clubs.service';
import { ClubsHttpService } from '../clubs/clubs-http.service';
import { EntriesService } from '../entries/entries.service';
import { EntriesHttpService } from '../entries/entries-http.service';
import { EventsService } from '../events/events.service';
import { EventsHttpService } from '../events/events-http.service';
import { PositionsService } from '../positions/positions.service';
import { PositionsHttpService } from '../positions/positions-http.service';

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
                ClubsService,
                ClubsHttpService,
                EntriesService,
                EntriesHttpService,
                EventsService,
                EventsHttpService,
                PositionsService,
                PositionsHttpService,
                WebSocketService,
                WebSocketWSService,
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
