import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Title } from '@angular/platform-browser';
import { Observable } from 'rxjs/Observable';

import { ResultsRacesService } from './races/races.service';

import { Race } from '../races/race.model';

@Component({
  selector: 'results',
  styleUrls: [
    './results.component.css'
  ],
  templateUrl: './results.component.html'
})
export class ResultsComponent implements OnInit {
  public logo = '/assets/img/stopwatch.svg';
  public tiatusUrl = 'https://github.com/PloughingAByteField/tiatus';
  public param = { value: 'world' };
  public races: Observable<Race[]>;

  constructor(
    private translate: TranslateService,
    private titleService: Title,
    private racesService: ResultsRacesService
  ) {}

  public ngOnInit() {
    this.translate.setDefaultLang('en');
    this.translate.use('en');
    this.races = this.racesService.getRaces();
    console.log(this.races);
  }

  public setTitle(newTitle: string) {
    this.translate.get(newTitle).subscribe((res: string) => {
      console.log('res is ', res);
      this.titleService.setTitle( res );
    });
  }

}
