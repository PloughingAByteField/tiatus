import { getTestBed, TestBed, async } from '@angular/core/testing';

import { Component, Injector, ChangeDetectorRef, ChangeDetectionStrategy, Injectable, ViewContainerRef } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Observable } from 'rxjs';

import { TranslateService, TranslateModule, TranslateLoader, TranslateFakeLoader } from '@ngx-translate/core';

import { LoginComponent } from './login.component';
import { LoginService } from './login.service';
import { LoginHttpService } from './login-http.service';
// import { FormBuilder } from '@angular/forms/src/form_builder';

let translations: any = { TEST: 'This is a test' };

// class FakeLoader implements TranslateLoader {
//   public getTranslation(lang: string): Observable<any> {
//     return Observable.of(translations);
//   }
// }

// class FakeTranslateService extends TranslateService {
// }

describe('LoginComponent', () => {
  let translate: any;
  let injector: Injector;
  let loginServiceStub = {};

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule, ReactiveFormsModule,
        TranslateModule.forRoot({loader: {provide: TranslateLoader, useClass: TranslateFakeLoader}})],
      // providers: [ {provide: TranslateService, use: FakeTranslateService}]
      // imports: [TranslateModule.forRoot()], providers: []
      providers: [  
        { provide: LoginService, use: loginServiceStub },
        { provide: LoginHttpService, use: {} }
       ]
    });

    injector = getTestBed();
    // translate = injector.get(TranslateService);

  });

  afterEach(() => {
    injector = undefined;
    translate = undefined;
    translations = { TEST: 'This is a test'};
    TestBed.resetTestingModule();
  });

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        LoginComponent
      ],
    }).compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(LoginComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
  xit(`should have as title 'app'`, async(() => {
    const fixture = TestBed.createComponent(LoginComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('app');
  }));
  xit('should render title in a h1 tag', async(() => {
    const fixture = TestBed.createComponent(LoginComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('hello');
  }));
});
