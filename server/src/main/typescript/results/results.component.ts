/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation } from '@angular/core';
import { TranslateService } from 'ng2-translate';
import { Title }     from '@angular/platform-browser';
import { RacesService } from '../services/races.service';
import { Observable } from 'rxjs/Observable';
import { Race } from '../models/race.model';

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
  logo = '/assets/img/stopwatch.svg';
  tiatusUrl = 'https://github.com/PloughingAByteField/tiatus';
  param = {value: 'world'};

  races: Observable<Race[]>;

  constructor(private translate: TranslateService, private titleService: Title, private racesService: RacesService) {
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

