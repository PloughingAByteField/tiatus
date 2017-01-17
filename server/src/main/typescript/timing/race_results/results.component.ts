import { Component } from '@angular/core';

@Component({
    selector: 'results',
    styleUrls: [ './results.component.css' ],
    templateUrl: './results.component.html'
})
export class ResultsComponent {

    ngOnInit() {
        console.log('hello from results');
    }
}