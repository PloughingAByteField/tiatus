import { Injectable } from '@angular/core';

import { BehaviorSubject, Subject } from 'rxjs';

import { ConfigHttpService } from './config-http.service';

@Injectable()
export class ConfigService {
    protected logo: BehaviorSubject<string> = new BehaviorSubject<string>(null);
    protected title: BehaviorSubject<string> = new BehaviorSubject<string>(null);
    protected footer: BehaviorSubject<string> = new BehaviorSubject<string>(null);

    private config: object;

    constructor(protected service: ConfigHttpService) {
        this.service.getConfig().subscribe((data: object) => {
            this.config = data;
            this.logo.next(location.origin + (data['logo']));
            this.title.next(data['title']);
            this.footer.next(data['footer']);
        });
    }

    public getFooter(): Subject<string>  {
        return this.footer;
    }

    public getLogo(): Subject<string>  {
        return this.logo;
    }

    public getEventTitle(): Subject<string> {
        return this.title;
    }

    public setTitle(title: string): void {
        this.title.next(title);
    }

    public setFooter(footer: string): void {
        this.footer.next(footer);
    }

    public setLogo(logo: string): void {
        this.logo.next(location.origin + logo);
    }
}
