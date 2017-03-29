import { Injectable } from '@angular/core';

import { ConfigService } from '../../config/config.service';
import { AdminConfigHttpService } from './config-http.service';

@Injectable()
export class AdminConfigService extends ConfigService {

    constructor(protected service: AdminConfigHttpService) {
        super(service);
    }

    public uploadTitle(title: string): Promise<string> {
        return new Promise((resolve) => this.service.uploadTitle(title).then((newTitle: string) => {
            this.title.next(newTitle);
            resolve(newTitle);
        }));
    }

    public uploadFooter(footer: string): Promise<string> {
        return new Promise((resolve) => this.service.uploadFooter(footer)
            .then((newFooter: string) => {
                this.footer.next(newFooter);
                resolve(newFooter);
        }));
    }

    public uploadLogo(file: File): Promise<string> {
        return new Promise((resolve) => this.service.uploadLogo(file).then((fileName: string) => {
            this.logo.next(fileName);
            resolve(fileName);
        }));
    }

}
