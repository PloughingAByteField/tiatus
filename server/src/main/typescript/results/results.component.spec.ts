import { TestBed, waitForAsync } from '@angular/core/testing';

import { ResultsComponent } from './results.component';

describe('ResultsComponent', () => {
  // beforeEach(async(() => {
  //   TestBed.configureTestingModule({
  //     declarations: [
  //       ResultsComponent
  //     ],
  //   }).compileComponents();
  // }));

  afterEach(() => {
    TestBed.resetTestingModule();
  });

  xit('should create the app', waitForAsync(() => {
    const fixture = TestBed.createComponent(ResultsComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
  xit(`should have as title 'app'`, waitForAsync(() => {
    const fixture = TestBed.createComponent(ResultsComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('app');
  }));
  xit('should render title in a h1 tag', waitForAsync(() => {
    const fixture = TestBed.createComponent(ResultsComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('hello');
  }));
});
