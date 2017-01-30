import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'race-results',
    styleUrls: [ './race-results.component.css' ],
    templateUrl: './race-results.component.html'
})
export class RaceResultsComponent implements OnInit {

    public ngOnInit() {
        console.log('hello from race results');
    }
}
