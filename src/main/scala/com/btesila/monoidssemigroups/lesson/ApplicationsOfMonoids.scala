/** ***********************************************************************
  * ADOBE CONFIDENTIAL
  * ___________________
  *
  * Copyright 2018 Adobe Systems Incorporated
  * All Rights Reserved.
  *
  * NOTICE:  All information contained herein is, and remains
  * the property of Adobe Systems Incorporated and its suppliers,
  * if any.  The intellectual and technical concepts contained
  * herein are proprietary to Adobe Systems Incorporated and its
  * suppliers and are protected by all applicable intellectual property
  * laws, including trade secret and copyright laws.
  * Dissemination of this information or reproduction of this material
  * is strictly forbidden unless prior written permission is obtained
  * from Adobe Systems Incorporated.
  * *************************************************************************/
package com.btesila.monoidssemigroups.lesson

object ApplicationsOfMonoids {
  /**
    * Big Data
    *
    * If we want to calculate how many total visitors a web site has received, that
    * means calculating an Int on each por􀦞on of the data. We know the monoid
    * instance of Int is addi􀦞on, which is the right way to combine partial results.
    *
    * Almost every analysis that we might want to do over a large data set is a monoid, and therefore
    * we can build an expressive and powerful analytics system around this idea. This is exactly
    * what Twitter’s Algebird and Summingbird projects have done. We explore this idea further
    * in the map-reduce case study.
    */

  /**
    * Distributed Systems
    *
    * In a distributed system, different machines may end up with different views of
    * data. For example, one machine may receive an update that other machines
    * did not receive. We would like to reconcile these different views, so every
    * machine has the same data if no more updates arrive. This is called eventual
    * consistency.
    *
    * A particular class of data types support this reconciliation. These data types
    * are called commutative replicated data types (CRDTs). The key operation is
    * the ability to merge two data instances, with a result that captures all the information in both
    * instances. This operation relies on having a monoid instance.
    * We explore this idea further in the CRDT case study.
    */

}
