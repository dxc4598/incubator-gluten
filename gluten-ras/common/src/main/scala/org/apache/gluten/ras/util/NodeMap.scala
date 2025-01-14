/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.gluten.ras.util

import org.apache.gluten.ras.{CanonicalNode, Ras}

import scala.collection.mutable

// Arbitrary node key.
class NodeKey[T <: AnyRef](ras: Ras[T], val node: T) {
  override def hashCode(): Int = ras.planModel.hashCode(node)

  override def equals(obj: Any): Boolean = {
    obj match {
      case other: NodeKey[T] => ras.planModel.equals(node, other.node)
      case _ => false
    }
  }

  override def toString(): String = s"NodeKey($node)"
}

// Canonical node map.
class CanonicalNodeMap[T <: AnyRef, V](ras: Ras[T]) {
  private val map: mutable.Map[NodeKey[T], V] = mutable.Map()

  def contains(node: CanonicalNode[T]): Boolean = {
    map.contains(keyOf(node))
  }

  def put(node: CanonicalNode[T], value: V): Unit = {
    map.put(keyOf(node), value)
  }

  def get(node: CanonicalNode[T]): V = {
    map(keyOf(node))
  }

  def getOrElseUpdate(node: CanonicalNode[T], op: => V): V = {
    map.getOrElseUpdate(keyOf(node), op)
  }

  private def keyOf(node: CanonicalNode[T]): NodeKey[T] = {
    new NodeKey(ras, node.self())
  }
}
