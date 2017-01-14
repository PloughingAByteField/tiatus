/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation } from '@angular/core';
import { TranslateService } from 'ng2-translate';
import { Title }     from '@angular/platform-browser';
import { AppState } from './app.service';


/*
 * App Component
 * Top Level Component
 */
@Component({
  selector: 'app',
  styleUrls: [
    './app.component.css'
  ],
  templateUrl: './app.html'
})
export class AppComponent {
  angularclassLogo = 'assets/img/angularclass-avatar.png';
  name = 'Angular 2 Webpack Starter';
  url = 'https://twitter.com/AngularClass';
  param = {value: 'world'};

  constructor(
    public appState: AppState, private translate: TranslateService, private titleService: Title) {

      translate.setDefaultLang('en');

     // the lang to use, if the lang isn't available, it will use the current loader to get them
     translate.use('en');
  }

  public setTitle( newTitle: string) {
    this.translate.get(newTitle).subscribe((res: string) => {
      console.log('res is ', res);
      this.titleService.setTitle( res );
    });
  }

  ngOnInit() {
    console.log('Initial App State', this.appState.state);
    this.setTitle('TITLE');
  }

}

/*
 * Please review the https://github.com/AngularClass/angular2-examples/ repo for
 * more angular app examples that you may copy/paste
 * (The examples may not be updated as quickly. Please open an issue on github for us to update it)
 * For help or questions please contact us at @AngularClass on twitter
 * or our chat on Slack at https://AngularClass.com/slack-join
 */
