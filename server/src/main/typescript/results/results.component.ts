/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Title }     from '@angular/platform-browser';
import { Observable } from 'rxjs/Observable';

import { RacesService } from '../races/races.service';

import { Race } from '../races/race.model';

/*
 * App Component
 * Top Level Component
 */
@Component({
  selector: 'results',
  styleUrls: [
    './results.component.css'
  ],
  templateUrl: './results.component.html'
})
export class ResultsComponent {
  public logo = '/assets/img/stopwatch.svg';
  public tiatusUrl = 'https://github.com/PloughingAByteField/tiatus';
  public param = { value: 'world' };
  public races: Observable<Race[]>;

  constructor(
    private translate: TranslateService,
    private titleService: Title,
    private racesService: RacesService
  ) {
      translate.setDefaultLang('en');

      // the lang to use, if the lang isn't available, it will use the current loader to get them
      translate.use('en');

      this.races = this.racesService.getRaces();
  }

  public setTitle( newTitle: string) {
    this.translate.get(newTitle).subscribe((res: string) => {
      console.log('res is ', res);
      this.titleService.setTitle( res );
    });
  }

}
