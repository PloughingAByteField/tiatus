/*
 * Angular 2 decorators and services
 */
import { Component, ViewEncapsulation } from '@angular/core';
import { TranslateService } from 'ng2-translate';
import { Title }     from '@angular/platform-browser';
import { RacesService } from '../http-services/races.service';
import { Observable } from 'rxjs/Observable';
import { Race } from '../models/race.model';

/*
 * App Component
 * Top Level Component
 */
@Component({
  selector: 'admin',
  styleUrls: [
    './admin.component.css'
  ],
  templateUrl: './admin.component.html'
})
export class AdminComponent {
  public logo = '/assets/img/stopwatch.svg';
  public tiatusUrl = 'https://github.com/PloughingAByteField/tiatus';
  public param = {value: 'world'};
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
