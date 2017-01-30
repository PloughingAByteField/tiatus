import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'results',
    styleUrls: [ './results.component.css' ],
    templateUrl: './results.component.html'
})
export class ResultsComponent implements OnInit {

    public ngOnInit() {
        console.log('hello from results');
    }
}
