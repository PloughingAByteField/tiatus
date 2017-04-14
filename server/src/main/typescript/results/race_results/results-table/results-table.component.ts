import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
    selector: 'race-results-table',
    styleUrls: [ './results-table.component.css' ],
    templateUrl: './results-table.component.html'
})
export class RaceResultsTableComponent implements OnInit, OnDestroy {

     constructor(
        private route: ActivatedRoute) {}

    public ngOnInit() {
        console.log('init');
        console.log(this.route.snapshot.paramMap);
        this.route.params.subscribe((params: Params) => {
            console.log(params['from']);
            console.log(params['to']);
        });
    }

    public ngOnDestroy() {
        console.log('bye');
    }
}
