/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation, OnInit } from '@angular/core';
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
  selector: 'adjudicator',
  styleUrls: [
    './adjudicator.component.css'
  ],
  templateUrl: './adjudicator.component.html'
})
export class AdjudicatorComponent implements OnInit {
  public races: Observable<Race[]>;

  constructor(
    private translate: TranslateService,
    private titleService: Title,
    private racesService: RacesService
  ) {}

  public ngOnInit() {
      console.log('hello from penalties');
      // the lang to use, if the lang isn't available, it will use the current loader to get them
      this.translate.use('en');

      this.races = this.racesService.getRaces();
  }
}
