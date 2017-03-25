/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation, OnInit } from '@angular/core';
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
      this.translate.use('en');

      this.races = this.racesService.getRaces();
  }
}
