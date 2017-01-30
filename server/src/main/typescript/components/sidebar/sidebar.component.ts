import { Component, Input } from '@angular/core';
import { TranslateService } from 'ng2-translate';
import { Observable } from 'rxjs/Observable';
import { Race } from '../../models/race.model';

@Component({
  selector: 'sidebar',
  styleUrls: [ './sidebar.component.css' ],
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent {
    private _races: Observable<Race[]>;
    private _link: string = 'race';

    constructor(private translate: TranslateService) {}

    @Input() set races(races: Observable<Race[]>) {
    console.log("races set called");
    console.log(races);
        this._races = races;
    }

    get races(): Observable<Race[]> {
        return this._races;
    }

    @Input() set link(link: string) {
    console.log("link set called " + link);
        this._link = link;
    }

    get link(): string {
        return this._link;
    }

}
