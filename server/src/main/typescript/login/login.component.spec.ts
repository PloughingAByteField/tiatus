import { inject, getTestBed, TestBed } from '@angular/core/testing';
import { Injector } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { XHRBackend, HttpModule } from '@angular/http';
import { MockConnection, MockBackend } from '@angular/http/testing';
import { Observable } from 'rxjs/Observable';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';

import { TranslateService, TranslateModule, TranslateLoader } from '@ngx-translate/core';

import { LoginComponent } from './login.component';
import { LoginService } from './login.service';
import { LoginHttpService } from './login-http.service';

let mockRouter = {
  navigate: jasmine.createSpy('navigate')
};

describe('Login', () => {
    let translate: any;
    let injector: Injector;
    let backend: any;
    let connection: MockConnection;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpModule, TranslateModule.forRoot()],
            providers: [
                { provide: XHRBackend, useClass: MockBackend },
                Title,
                LoginComponent,
                LoginService,
                LoginHttpService,
                FormBuilder,
                { provide: Router, useValue: mockRouter }
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

    it('should have loginForm created', inject([ LoginComponent ], (app: LoginComponent) => {
        expect(app.loginForm).not.toBeNull();
  }));

});
