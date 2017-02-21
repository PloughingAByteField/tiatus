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
    private shouldShowBack: boolean = false;

    constructor(private translate: TranslateService) {}

    @Input() set races(newRaces: Observable<Race[]>) {
        this.racesArray = newRaces;
    }

    get races(): Observable<Race[]> {
        return this.racesArray;
    }

    @Input() set link(newLink: string) {
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

}
