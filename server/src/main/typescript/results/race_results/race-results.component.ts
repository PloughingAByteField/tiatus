import { Component } from '@angular/core';

@Component({
    selector: 'race-results',
    styleUrls: [ './race-results.component.css' ],
    templateUrl: './race-results.component.html'
})
export class RaceResultsComponent {

    ngOnInit() {
        console.log('hello from race results');
    }
}