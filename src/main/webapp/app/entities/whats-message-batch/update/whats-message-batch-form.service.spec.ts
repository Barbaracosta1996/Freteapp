import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../whats-message-batch.test-samples';

import { WhatsMessageBatchFormService } from './whats-message-batch-form.service';

describe('WhatsMessageBatch Form Service', () => {
  let service: WhatsMessageBatchFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WhatsMessageBatchFormService);
  });

  describe('Service methods', () => {
    describe('createWhatsMessageBatchFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWhatsMessageBatchFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            waidTo: expect.any(Object),
            perfilID: expect.any(Object),
            status: expect.any(Object),
            ofertaId: expect.any(Object),
            tipoOferta: expect.any(Object),
            notificationDate: expect.any(Object),
            createdDate: expect.any(Object),
          })
        );
      });

      it('passing IWhatsMessageBatch should create a new form with FormGroup', () => {
        const formGroup = service.createWhatsMessageBatchFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            waidTo: expect.any(Object),
            perfilID: expect.any(Object),
            status: expect.any(Object),
            ofertaId: expect.any(Object),
            tipoOferta: expect.any(Object),
            notificationDate: expect.any(Object),
            createdDate: expect.any(Object),
          })
        );
      });
    });

    describe('getWhatsMessageBatch', () => {
      it('should return NewWhatsMessageBatch for default WhatsMessageBatch initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createWhatsMessageBatchFormGroup(sampleWithNewData);

        const whatsMessageBatch = service.getWhatsMessageBatch(formGroup) as any;

        expect(whatsMessageBatch).toMatchObject(sampleWithNewData);
      });

      it('should return NewWhatsMessageBatch for empty WhatsMessageBatch initial value', () => {
        const formGroup = service.createWhatsMessageBatchFormGroup();

        const whatsMessageBatch = service.getWhatsMessageBatch(formGroup) as any;

        expect(whatsMessageBatch).toMatchObject({});
      });

      it('should return IWhatsMessageBatch', () => {
        const formGroup = service.createWhatsMessageBatchFormGroup(sampleWithRequiredData);

        const whatsMessageBatch = service.getWhatsMessageBatch(formGroup) as any;

        expect(whatsMessageBatch).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWhatsMessageBatch should not enable id FormControl', () => {
        const formGroup = service.createWhatsMessageBatchFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWhatsMessageBatch should disable id FormControl', () => {
        const formGroup = service.createWhatsMessageBatchFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
