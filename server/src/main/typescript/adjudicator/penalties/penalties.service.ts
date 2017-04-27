import { Injectable } from '@angular/core';

import { Penalty, convertObjectToPenalty } from '../../penalties/penalty.model';
import { Entry } from '../../entries/entry.model';

import { PenaltiesService } from '../../penalties/penalties.service';
import { AdjudicatorHttpPenaltiesService } from './penalties-http.service';

import { Message } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';

@Injectable()
export class AdjudicatorPenaltiesService extends PenaltiesService {

    constructor(protected service: AdjudicatorHttpPenaltiesService) {
        super(service);
    }

    public createPenalty(penalty: Penalty): void {
        this.service.createPenalty(penalty).then(() => {
            this.penalties.push(penalty);
            this.penaltiesSubject.next(this.penalties);
        });
    }

    public removePenalty(penalty: Penalty): void {
        this.service.removePenalty(penalty).then(() => {
            const index = this.penalties.indexOf(penalty);
            const sliced = this.penalties.splice(index, 1);
            this.penaltiesSubject.next(this.penalties);
        });
    }

    public updatePenalty(penalty: Penalty): void {
        this.service.updatePenalty(penalty).then();
    }

    public getPenaltiesForEntry(entry: Entry): Penalty[] {
        let penalties: Penalty[];
        if (this.penalties) {
            penalties = this.penalties.filter((penalty: Penalty) => penalty.entry === entry.id);
        }

        return penalties;
    }

    public processPenaltyMessage(message: Message): void {
        console.log('process message');
        const penalty: Penalty = convertObjectToPenalty(message.data);
        console.log(penalty);
        if (message.type === MessageType.ADD) {
            this.penalties.push(penalty);
        } else if (message.type === MessageType.DELETE) {
            const deletedPenalty: Penalty = this.getPenaltyForId(penalty.id);
            if (deletedPenalty !== null) {
                const index = this.penalties.indexOf(deletedPenalty);
                const sliced = this.penalties.splice(index, 1);
            }
        } else if (message.type === MessageType.UPDATE) {
            const updatedPenalty: Penalty = this.getPenaltyForId(penalty.id);
            if (updatedPenalty !== null) {
                updatedPenalty.comment = penalty.comment;
                updatedPenalty.entry = penalty.entry;
                updatedPenalty.note = penalty.note;
                updatedPenalty.time = penalty.time;
            }
        }

        this.penaltiesSubject.next(this.penalties);
    }

    public getPenaltyForId(penaltyId: number): Penalty {
        for (const penalty of this.penalties) {
            if (penalty.id === penaltyId) {
                return penalty;
            }
        }
        return null;
    }
}
