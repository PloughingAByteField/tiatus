import { Component, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { Race } from '../../races/race.model';

@Component({
  selector: 'sidebar',
  styleUrls: [ './sidebar.component.css' ],
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent {
    private racesArray: Observable<Race[]>;
    private raceLink: string = 'race';
    private shouldShowBack: boolean = false;
    private shouldShowLogout: boolean = false;

    constructor(private translate: TranslateService) {}

    @Input() set races(newRaces: Observable<Race[]>) {
        this.racesArray = newRaces;
    }

    get races(): Observable<Race[]> {
        return this.racesArray;
    }

    @Input() set link(newLink: string) {
        console.log("link now " + newLink);
        this.raceLink = newLink;
    }

    get link(): string {
        return this.raceLink;
    }

    @Input() set showBack(show: boolean) {
        this.shouldShowBack = show;
    }

    get showBack(): boolean {
        return this.shouldShowBack;
    }

    @Input() set showLogout(show: boolean) {
        this.shouldShowLogout = show;
    }

    get showLogout(): boolean {
        return this.shouldShowLogout;
    }
}
