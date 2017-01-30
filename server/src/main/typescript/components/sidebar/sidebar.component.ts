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
    private racesArray: Observable<Race[]>;
    private raceLink: string = 'race';

    constructor(private translate: TranslateService) {}

    @Input() set races(newRaces: Observable<Race[]>) {
    console.log("races set called");
    console.log(newRaces);
        this.racesArray = newRaces;
    }

    get races(): Observable<Race[]> {
        return this.racesArray;
    }

    @Input() set link(newLink: string) {
    console.log("link set called " + newLink);
        this.raceLink = newLink;
    }

    get link(): string {
        return this.raceLink;
    }

}
