import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../solicitacao.test-samples';

import { SolicitacaoFormService } from './solicitacao-form.service';

describe('Solicitacao Form Service', () => {
  let service: SolicitacaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SolicitacaoFormService);
  });

  describe('Service methods', () => {
    describe('createSolicitacaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSolicitacaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataProposta: expect.any(Object),
            dataModificacao: expect.any(Object),
            aceitar: expect.any(Object),
            status: expect.any(Object),
            obs: expect.any(Object),
            valorFrete: expect.any(Object),
            ofertas: expect.any(Object),
            perfil: expect.any(Object),
            minhaOferta: expect.any(Object),
            requestedPerfil: expect.any(Object),
          })
        );
      });

      it('passing ISolicitacao should create a new form with FormGroup', () => {
        const formGroup = service.createSolicitacaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dataProposta: expect.any(Object),
            dataModificacao: expect.any(Object),
            aceitar: expect.any(Object),
            status: expect.any(Object),
            obs: expect.any(Object),
            valorFrete: expect.any(Object),
            ofertas: expect.any(Object),
            perfil: expect.any(Object),
            minhaOferta: expect.any(Object),
            requestedPerfil: expect.any(Object),
          })
        );
      });
    });

    describe('getSolicitacao', () => {
      it('should return NewSolicitacao for default Solicitacao initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSolicitacaoFormGroup(sampleWithNewData);

        const solicitacao = service.getSolicitacao(formGroup) as any;

        expect(solicitacao).toMatchObject(sampleWithNewData);
      });

      it('should return NewSolicitacao for empty Solicitacao initial value', () => {
        const formGroup = service.createSolicitacaoFormGroup();

        const solicitacao = service.getSolicitacao(formGroup) as any;

        expect(solicitacao).toMatchObject({});
      });

      it('should return ISolicitacao', () => {
        const formGroup = service.createSolicitacaoFormGroup(sampleWithRequiredData);

        const solicitacao = service.getSolicitacao(formGroup) as any;

        expect(solicitacao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISolicitacao should not enable id FormControl', () => {
        const formGroup = service.createSolicitacaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSolicitacao should disable id FormControl', () => {
        const formGroup = service.createSolicitacaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
