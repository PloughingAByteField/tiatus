import { inject, getTestBed, TestBed } from '@angular/core/testing';
import { Injector } from '@angular/core';
import { XHRBackend, HttpModule } from '@angular/http';
import { MockConnection, MockBackend } from '@angular/http/testing';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs/Observable';

import { TranslateService, TranslateModule, TranslateLoader } from '@ngx-translate/core';

import { WebSocketService } from '../websocket/websocket-service';
import { WebSocketWSService } from '../websocket/websocket-ws-service';
import { AdminWebSocketService } from './websocket/websocket-service';
import { AdminRacesService } from './races/races.service';
import { AdminRacesHttpService } from './races/races-http.service';
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
                })
            ],
            providers: [
                {provide: XHRBackend, useClass: MockBackend},
                AdminRacesService,
                AdminRacesHttpService,
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
        expect(app.tiatusUrl).toEqual('https://github.com/PloughingAByteField/tiatus');
    }));

});
