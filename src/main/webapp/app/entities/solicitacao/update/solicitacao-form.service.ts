import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISolicitacao, NewSolicitacao } from '../solicitacao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISolicitacao for edit and NewSolicitacaoFormGroupInput for create.
 */
type SolicitacaoFormGroupInput = ISolicitacao | PartialWithRequiredKeyOf<NewSolicitacao>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISolicitacao | NewSolicitacao> = Omit<T, 'dataProposta' | 'dataModificacao'> & {
  dataProposta?: string | null;
  dataModificacao?: string | null;
};

type SolicitacaoFormRawValue = FormValueOf<ISolicitacao>;

type NewSolicitacaoFormRawValue = FormValueOf<NewSolicitacao>;

type SolicitacaoFormDefaults = Pick<NewSolicitacao, 'id' | 'dataProposta' | 'dataModificacao'>;

type SolicitacaoFormGroupContent = {
  id: FormControl<SolicitacaoFormRawValue['id'] | NewSolicitacao['id']>;
  dataProposta: FormControl<SolicitacaoFormRawValue['dataProposta']>;
  dataModificacao: FormControl<SolicitacaoFormRawValue['dataModificacao']>;
  aceitar: FormControl<SolicitacaoFormRawValue['aceitar']>;
  status: FormControl<SolicitacaoFormRawValue['status']>;
  obs: FormControl<SolicitacaoFormRawValue['obs']>;
  valorFrete: FormControl<SolicitacaoFormRawValue['valorFrete']>;
  ofertas: FormControl<SolicitacaoFormRawValue['ofertas']>;
  perfil: FormControl<SolicitacaoFormRawValue['perfil']>;
};

export type SolicitacaoFormGroup = FormGroup<SolicitacaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SolicitacaoFormService {
  createSolicitacaoFormGroup(solicitacao: SolicitacaoFormGroupInput = { id: null }): SolicitacaoFormGroup {
    const solicitacaoRawValue = this.convertSolicitacaoToSolicitacaoRawValue({
      ...this.getFormDefaults(),
      ...solicitacao,
    });
    return new FormGroup<SolicitacaoFormGroupContent>({
      id: new FormControl(
        { value: solicitacaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dataProposta: new FormControl(solicitacaoRawValue.dataProposta, {
        validators: [Validators.required],
      }),
      dataModificacao: new FormControl(solicitacaoRawValue.dataModificacao),
      aceitar: new FormControl(solicitacaoRawValue.aceitar),
      status: new FormControl(solicitacaoRawValue.status, {
        validators: [Validators.required],
      }),
      obs: new FormControl(solicitacaoRawValue.obs),
      valorFrete: new FormControl(solicitacaoRawValue.valorFrete),
      ofertas: new FormControl(solicitacaoRawValue.ofertas, {
        validators: [Validators.required],
      }),
      perfil: new FormControl(solicitacaoRawValue.perfil, {
        validators: [Validators.required],
      }),
    });
  }

  getSolicitacao(form: SolicitacaoFormGroup): ISolicitacao | NewSolicitacao {
    return this.convertSolicitacaoRawValueToSolicitacao(form.getRawValue() as SolicitacaoFormRawValue | NewSolicitacaoFormRawValue);
  }

  resetForm(form: SolicitacaoFormGroup, solicitacao: SolicitacaoFormGroupInput): void {
    const solicitacaoRawValue = this.convertSolicitacaoToSolicitacaoRawValue({ ...this.getFormDefaults(), ...solicitacao });
    form.reset(
      {
        ...solicitacaoRawValue,
        id: { value: solicitacaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SolicitacaoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataProposta: currentTime,
      dataModificacao: currentTime,
    };
  }

  private convertSolicitacaoRawValueToSolicitacao(
    rawSolicitacao: SolicitacaoFormRawValue | NewSolicitacaoFormRawValue
  ): ISolicitacao | NewSolicitacao {
    return {
      ...rawSolicitacao,
      dataProposta: dayjs(rawSolicitacao.dataProposta, DATE_TIME_FORMAT),
      dataModificacao: dayjs(rawSolicitacao.dataModificacao, DATE_TIME_FORMAT),
    };
  }

  private convertSolicitacaoToSolicitacaoRawValue(
    solicitacao: ISolicitacao | (Partial<NewSolicitacao> & SolicitacaoFormDefaults)
  ): SolicitacaoFormRawValue | PartialWithRequiredKeyOf<NewSolicitacaoFormRawValue> {
    return {
      ...solicitacao,
      dataProposta: solicitacao.dataProposta ? solicitacao.dataProposta.format(DATE_TIME_FORMAT) : undefined,
      dataModificacao: solicitacao.dataModificacao ? solicitacao.dataModificacao.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
