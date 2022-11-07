import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWhatsMessageBatch, NewWhatsMessageBatch } from '../whats-message-batch.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWhatsMessageBatch for edit and NewWhatsMessageBatchFormGroupInput for create.
 */
type WhatsMessageBatchFormGroupInput = IWhatsMessageBatch | PartialWithRequiredKeyOf<NewWhatsMessageBatch>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWhatsMessageBatch | NewWhatsMessageBatch> = Omit<T, 'notificationDate' | 'createdDate'> & {
  notificationDate?: string | null;
  createdDate?: string | null;
};

type WhatsMessageBatchFormRawValue = FormValueOf<IWhatsMessageBatch>;

type NewWhatsMessageBatchFormRawValue = FormValueOf<NewWhatsMessageBatch>;

type WhatsMessageBatchFormDefaults = Pick<NewWhatsMessageBatch, 'id' | 'notificationDate' | 'createdDate'>;

type WhatsMessageBatchFormGroupContent = {
  id: FormControl<WhatsMessageBatchFormRawValue['id'] | NewWhatsMessageBatch['id']>;
  tipo: FormControl<WhatsMessageBatchFormRawValue['tipo']>;
  waidTo: FormControl<WhatsMessageBatchFormRawValue['waidTo']>;
  perfilID: FormControl<WhatsMessageBatchFormRawValue['perfilID']>;
  status: FormControl<WhatsMessageBatchFormRawValue['status']>;
  ofertaId: FormControl<WhatsMessageBatchFormRawValue['ofertaId']>;
  tipoOferta: FormControl<WhatsMessageBatchFormRawValue['tipoOferta']>;
  notificationDate: FormControl<WhatsMessageBatchFormRawValue['notificationDate']>;
  createdDate: FormControl<WhatsMessageBatchFormRawValue['createdDate']>;
};

export type WhatsMessageBatchFormGroup = FormGroup<WhatsMessageBatchFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WhatsMessageBatchFormService {
  createWhatsMessageBatchFormGroup(whatsMessageBatch: WhatsMessageBatchFormGroupInput = { id: null }): WhatsMessageBatchFormGroup {
    const whatsMessageBatchRawValue = this.convertWhatsMessageBatchToWhatsMessageBatchRawValue({
      ...this.getFormDefaults(),
      ...whatsMessageBatch,
    });
    return new FormGroup<WhatsMessageBatchFormGroupContent>({
      id: new FormControl(
        { value: whatsMessageBatchRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tipo: new FormControl(whatsMessageBatchRawValue.tipo, {
        validators: [Validators.required],
      }),
      waidTo: new FormControl(whatsMessageBatchRawValue.waidTo, {
        validators: [Validators.required],
      }),
      perfilID: new FormControl(whatsMessageBatchRawValue.perfilID, {
        validators: [Validators.required],
      }),
      status: new FormControl(whatsMessageBatchRawValue.status, {
        validators: [Validators.required],
      }),
      ofertaId: new FormControl(whatsMessageBatchRawValue.ofertaId),
      tipoOferta: new FormControl(whatsMessageBatchRawValue.tipoOferta),
      notificationDate: new FormControl(whatsMessageBatchRawValue.notificationDate),
      createdDate: new FormControl(whatsMessageBatchRawValue.createdDate),
    });
  }

  getWhatsMessageBatch(form: WhatsMessageBatchFormGroup): IWhatsMessageBatch | NewWhatsMessageBatch {
    return this.convertWhatsMessageBatchRawValueToWhatsMessageBatch(
      form.getRawValue() as WhatsMessageBatchFormRawValue | NewWhatsMessageBatchFormRawValue
    );
  }

  resetForm(form: WhatsMessageBatchFormGroup, whatsMessageBatch: WhatsMessageBatchFormGroupInput): void {
    const whatsMessageBatchRawValue = this.convertWhatsMessageBatchToWhatsMessageBatchRawValue({
      ...this.getFormDefaults(),
      ...whatsMessageBatch,
    });
    form.reset(
      {
        ...whatsMessageBatchRawValue,
        id: { value: whatsMessageBatchRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): WhatsMessageBatchFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      notificationDate: currentTime,
      createdDate: currentTime,
    };
  }

  private convertWhatsMessageBatchRawValueToWhatsMessageBatch(
    rawWhatsMessageBatch: WhatsMessageBatchFormRawValue | NewWhatsMessageBatchFormRawValue
  ): IWhatsMessageBatch | NewWhatsMessageBatch {
    return {
      ...rawWhatsMessageBatch,
      notificationDate: dayjs(rawWhatsMessageBatch.notificationDate, DATE_TIME_FORMAT),
      createdDate: dayjs(rawWhatsMessageBatch.createdDate, DATE_TIME_FORMAT),
    };
  }

  private convertWhatsMessageBatchToWhatsMessageBatchRawValue(
    whatsMessageBatch: IWhatsMessageBatch | (Partial<NewWhatsMessageBatch> & WhatsMessageBatchFormDefaults)
  ): WhatsMessageBatchFormRawValue | PartialWithRequiredKeyOf<NewWhatsMessageBatchFormRawValue> {
    return {
      ...whatsMessageBatch,
      notificationDate: whatsMessageBatch.notificationDate ? whatsMessageBatch.notificationDate.format(DATE_TIME_FORMAT) : undefined,
      createdDate: whatsMessageBatch.createdDate ? whatsMessageBatch.createdDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
