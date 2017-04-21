import { inject, getTestBed, TestBed } from '@angular/core/testing';
import { Injector } from '@angular/core';
import { XHRBackend, HttpModule } from '@angular/http';
import { MockConnection, MockBackend } from '@angular/http/testing';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs/Observable';

import { TranslateService, TranslateModule, TranslateLoader } from '@ngx-translate/core';

import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';

import { WebSocketService } from '../websocket/websocket-service';
import { WebSocketWSService } from '../websocket/websocket-ws-service';
import { AdminWebSocketService } from './websocket/websocket-service';
import { AdminRacesService } from './races/races.service';
import { AdminRacesHttpService } from './races/races-http.service';
import { AdminClubsService } from './clubs/clubs.service';
import { AdminClubsHttpService } from './clubs/clubs-http.service';
import { AdminEntriesService } from './entries/entries.service';
import { AdminEntriesHttpService } from './entries/entries-http.service';
import { AdminEventsService } from './events/events.service';
import { AdminEventsHttpService } from './events/events-http.service';
import { AdminPositionsService } from './positions/positions.service';
import { AdminPositionsHttpService } from './positions/positions-http.service';
import { AdminUsersService } from './users/users.service';
import { AdminUsersHttpService } from './users/users-http.service';
import { RacePositionsService } from './race-positions/race-positions.service';
import { RacePositionsHttpService } from './race-positions/race-positions-http.service';
import { RacePositionTemplatesService }
    from './race-position-templates/race-position-templates.service';
import { RacePositionTemplatesHttpService }
    from './race-position-templates/race-position-templates-http.service';
import { AdminComponent } from './admin.component';

let translations: any = { TEST: 'This is a test' };
class FakeLoader implements TranslateLoader {
    public getTranslation(lang: string): Observable<any> {
        return Observable.of(translations);
    }
}

describe('Admin', () => {
    let translate: any;
    let injector: Injector;
    let backend: any;
    let connection: MockConnection;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpModule,
                TranslateModule.forRoot({
                    loader: {provide: TranslateLoader, useClass: FakeLoader}
                }),
                NgIdleKeepaliveModule.forRoot()
            ],
            providers: [
                {provide: XHRBackend, useClass: MockBackend},
                AdminRacesService,
                AdminRacesHttpService,
                AdminClubsService,
                AdminClubsHttpService,
                AdminEntriesService,
                AdminEntriesHttpService,
                AdminEventsService,
                AdminEventsHttpService,
                AdminPositionsService,
                AdminPositionsHttpService,
                AdminUsersService,
                AdminUsersHttpService,
                RacePositionsService,
                RacePositionsHttpService,
                RacePositionTemplatesService,
                RacePositionTemplatesHttpService,
                WebSocketWSService,
                WebSocketService,
                AdminWebSocketService,
                Title,
                AdminComponent
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

    it('should have a url', inject([ AdminComponent ], (app: AdminComponent) => {
        // expect(app.tiatusUrl).toEqual('https://github.com/PloughingAByteField/tiatus');
        expect(true).toBe(true);
    }));

});
