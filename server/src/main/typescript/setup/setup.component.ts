import { Component, ViewEncapsulation, OnInit } from '@angular/core';

import { Title }     from '@angular/platform-browser';

import { TranslateService } from '@ngx-translate/core';

import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'setup',
  styleUrls: [
    './setup.component.css'
  ],
  templateUrl: './setup.component.html'
})
export class SetupComponent implements OnInit {
  public logo = '/assets/img/stopwatch.svg';
  public tiatusUrl = 'https://github.com/PloughingAByteField/tiatus';

  constructor(
    private translate: TranslateService,
    private titleService: Title,
  ) {
    translate.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('en');
  }

  public ngOnInit() {
    console.log('init');
  }

}
