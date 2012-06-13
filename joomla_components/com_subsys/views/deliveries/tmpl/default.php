<?php defined('_JEXEC') or die('Restricted access'); 
?>
<form action="index.php" method="post" name="adminForm">
<div id="editcell">
<table>
<tr>
	  <td align="left">
				<?php 
				$options = array();
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A SUBSCRIBER'));
    dump($this->subscribers, "SUBSCRIBERS");
    foreach($this->subscribers as $key=>$value) :
      $options[] = JHTML::_('select.option', $value->sub_code, JText::_($value->sub_name).'('.$value->sub_code.')');
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'sub_code', '', 'value', 'text', $this->lists['subcode']);
				?>
			</td>
			
			<td align="left">
				<?php 
				$options = array();
    $options[] = JHTML::_('select.option','',JText::_('PLS CHOOSE A PUBLICATION'));
    dump($this->publications, "PUBLICATIONS");
    foreach($this->publications as $key=>$value) :
      $options[] = JHTML::_('select.option', $value->pub_code, JText::_($value->pub_name).'('.$value->pub_code.')');
    endforeach;
    echo JHTML::_('select.genericlist', $options, 'pub_code', '', 'value', 'text', $this->lists['pubcode']);
				?>
				</td>
				<td align="left">
				<button onclick="this.form.submit();"><?php echo JText::_( 'Go' ); ?></button>
				<button onclick="document.getElementById('sub_code').value='';document.getElementById('pub_code').value='';this.form.submit();"><?php echo JText::_( 'Reset' ); ?></button>
			</td>
			
	</tr>
</table>
<?php if(!empty($this->lists)) { ?>
	<table class="adminlist">
	<thead>
		<tr>
			<th width="5">
				<?php echo JText::_( 'Database ID' ); ?>
			</th>
			<th width="20">
				<input type="checkbox" name="toggle" value="" onclick="checkAll(<?php echo count( $this->items ); ?>);" />
			</th>			
			<th>
				<?php echo JText::_( 'Delivery Date' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Order Code' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Subscriber' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Publication' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Title' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Issue' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Copies' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Delivered by' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Received by' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Delivered' ); ?>
			</th>					
			<th>
				<?php echo JText::_( 'Created/Modified Date' ); ?>
			</th>
		</tr>
	</thead>
	<?php
	$k = 0;
	for ($i=0, $n=count( $this->items ); $i < $n; $i++)	{
		$row =& $this->items[$i];
		$checked 	= JHTML::_('grid.id',   $i, $row->delivery_id );
		$link 		= JRoute::_( 'index.php?option=com_subsys&controller=delivery&task=edit&cid[]='. $row->delivery_id );		
		?>
		<tr class="<?php echo "row$k"; ?>">
			<td>
				<?php echo $row->delivery_id; ?>
			</td>
			<td>
				<?php echo $checked; ?>
			</td>
			<td>
				<?php echo $row->delivery_date; ?>
			</td>
			<td>
				<?php echo $row->order_code; ?>
			</td>
			<td>
				<?php echo $row->sub_name; ?>
			</td>	
			<td>
				<?php echo $row->pub_name; ?>
			</td>
			<td>
				<?php echo $row->delivery_title; ?>
			</td>
			<td>
				<?php echo $row->delivery_issue; ?>
			</td>
			<td>
				<?php echo $row->delivery_copies; ?>
			</td>
			<td>
				<?php echo $row->delivered_by; ?>
			</td>
			<td>
				<?php echo $row->received_by; ?>
			</td>		
			<td>
				<?php echo $row->delivery_done; ?>
			</td>								
			<td>
				<?php echo $row->cdate.'('.$row->mdate.')'; ?>
			</td>
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>
	<tfoot>
    <tr>
      <td colspan="15"><?php echo $this->pagination->getListFooter(); ?></td>
    </tr>
  </tfoot>
	</table>
	<?php } ?>
</div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="boxchecked" value="0" />
<input type="hidden" name="controller" value="delivery" />
</form>
