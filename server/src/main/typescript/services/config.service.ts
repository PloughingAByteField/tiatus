import { Injectable } from '@angular/core';

@Injectable()
export class ConfigService {
    private logo = '/tiatus/stopwatch.svg';
    private title = 'Event title';

    constructor() { console.log('config'); }

    public getLogo(): string {
        return this.logo;
    }

    public getEventTitle(): string {
        return this.title;
    }
}
