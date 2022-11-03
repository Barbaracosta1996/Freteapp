import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWhatsMessageBatch } from '../whats-message-batch.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../whats-message-batch.test-samples';

import { WhatsMessageBatchService } from './whats-message-batch.service';

const requireRestSample: IWhatsMessageBatch = {
  ...sampleWithRequiredData,
};

describe('WhatsMessageBatch Service', () => {
  let service: WhatsMessageBatchService;
  let httpMock: HttpTestingController;
  let expectedResult: IWhatsMessageBatch | IWhatsMessageBatch[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WhatsMessageBatchService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a WhatsMessageBatch', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const whatsMessageBatch = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(whatsMessageBatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WhatsMessageBatch', () => {
      const whatsMessageBatch = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(whatsMessageBatch).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WhatsMessageBatch', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WhatsMessageBatch', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WhatsMessageBatch', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWhatsMessageBatchToCollectionIfMissing', () => {
      it('should add a WhatsMessageBatch to an empty array', () => {
        const whatsMessageBatch: IWhatsMessageBatch = sampleWithRequiredData;
        expectedResult = service.addWhatsMessageBatchToCollectionIfMissing([], whatsMessageBatch);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(whatsMessageBatch);
      });

      it('should not add a WhatsMessageBatch to an array that contains it', () => {
        const whatsMessageBatch: IWhatsMessageBatch = sampleWithRequiredData;
        const whatsMessageBatchCollection: IWhatsMessageBatch[] = [
          {
            ...whatsMessageBatch,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWhatsMessageBatchToCollectionIfMissing(whatsMessageBatchCollection, whatsMessageBatch);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WhatsMessageBatch to an array that doesn't contain it", () => {
        const whatsMessageBatch: IWhatsMessageBatch = sampleWithRequiredData;
        const whatsMessageBatchCollection: IWhatsMessageBatch[] = [sampleWithPartialData];
        expectedResult = service.addWhatsMessageBatchToCollectionIfMissing(whatsMessageBatchCollection, whatsMessageBatch);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(whatsMessageBatch);
      });

      it('should add only unique WhatsMessageBatch to an array', () => {
        const whatsMessageBatchArray: IWhatsMessageBatch[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const whatsMessageBatchCollection: IWhatsMessageBatch[] = [sampleWithRequiredData];
        expectedResult = service.addWhatsMessageBatchToCollectionIfMissing(whatsMessageBatchCollection, ...whatsMessageBatchArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const whatsMessageBatch: IWhatsMessageBatch = sampleWithRequiredData;
        const whatsMessageBatch2: IWhatsMessageBatch = sampleWithPartialData;
        expectedResult = service.addWhatsMessageBatchToCollectionIfMissing([], whatsMessageBatch, whatsMessageBatch2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(whatsMessageBatch);
        expect(expectedResult).toContain(whatsMessageBatch2);
      });

      it('should accept null and undefined values', () => {
        const whatsMessageBatch: IWhatsMessageBatch = sampleWithRequiredData;
        expectedResult = service.addWhatsMessageBatchToCollectionIfMissing([], null, whatsMessageBatch, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(whatsMessageBatch);
      });

      it('should return initial array if no WhatsMessageBatch is added', () => {
        const whatsMessageBatchCollection: IWhatsMessageBatch[] = [sampleWithRequiredData];
        expectedResult = service.addWhatsMessageBatchToCollectionIfMissing(whatsMessageBatchCollection, undefined, null);
        expect(expectedResult).toEqual(whatsMessageBatchCollection);
      });
    });

    describe('compareWhatsMessageBatch', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWhatsMessageBatch(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWhatsMessageBatch(entity1, entity2);
        const compareResult2 = service.compareWhatsMessageBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWhatsMessageBatch(entity1, entity2);
        const compareResult2 = service.compareWhatsMessageBatch(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWhatsMessageBatch(entity1, entity2);
        const compareResult2 = service.compareWhatsMessageBatch(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
